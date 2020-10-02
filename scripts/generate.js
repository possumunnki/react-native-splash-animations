#!/usr/bin/env node

"use strict";

const path = require("path");
const fs = require("fs");
const chalk = require("chalk");
const jimp = require("jimp");
const prompts = require("prompts");

let projectName;

const logoFileName = "splash_animation_logo";
const androidColorRegex = /<color name="splash_animation_background">#\w+<\/color>/g;

const initialProjectPath = path.join(
  ".",
  path.relative(
    process.cwd(),
    path.resolve(path.join(__dirname, "..", "..", "..")),
  ),
);

const drawableXml = `<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android" android:opacity="opaque">
    <item android:drawable="@color/splash_animation_background" />
    <item android:id="@+id/logo">
        <bitmap android:src="@mipmap/${logoFileName}" android:gravity="center"/>
    </item>
</layer-list>
`;

const log = (text, dim = false) => {
  console.log(dim ? chalk.dim(text) : text);
};

const ensureDir = (dir) => {
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir);
  }
};

const isValidHexadecimal = (value) => /^#?([0-9A-F]{3}){1,2}$/i.test(value);

const toFullHexadecimal = (hex) => {
  const prefixed = hex[0] === "#" ? hex : `#${hex}`;
  const up = prefixed.toUpperCase();

  return up.length === 4
    ? "#" + up[1] + up[1] + up[2] + up[2] + up[3] + up[3]
    : up;
};

const getProjectName = (projectPath) => {
  try {
    const appJsonPath = path.join(projectPath, "app.json");
    const appJson = fs.readFileSync(appJsonPath, "utf-8");
    const { name } = JSON.parse(appJson);

    if (!name) {
      throw new Error("Invalid projectPath");
    }

    return name;
  } catch (e) {
    return false;
  }
};

const questions = [
  {
    name: "projectPath",
    type: "text",
    initial: initialProjectPath,
    message: "The path to the root of your React Native project",

    validate: (value) => {
      if (!fs.existsSync(value)) {
        return `Invalid project path. The directory ${chalk.bold(
          value,
        )} could not be found.`;
      }

      projectName = getProjectName(value);

      if (!projectName) {
        return `Invalid React Native project. A valid ${chalk.bold(
          "app.json",
        )} file could not be found.`;
      }

      return true;
    },
  },
  {
    name: "assetsPath",
    type: "text",
    initial: (prev) => path.join(prev, "assets"),
    message: "The path to your static assets directory",

    validate: (value) => {
      if (!fs.existsSync(value)) {
        return `Invalid assets path. The directory ${chalk.bold(
          value,
        )} could not be found.`;
      }

      return true;
    },
  },
  {
    name: "iconPath",
    type: "text",
    message: "Your original icon file",
    initial: (prev) => path.join(prev, `${logoFileName}_original.png`),

    validate: (value) => {
      if (!fs.existsSync(value)) {
        return `Invalid icon file path. The file ${chalk.bold(
          value,
        )} could not be found.`;
      }

      return true;
    },
  },
  {
    name: "backgroundColor",
    type: "text",
    message: "The splashanimation background color (in hexadecimal)",
    initial: "#FFF",

    validate: (value) => {
      if (!isValidHexadecimal(value)) {
        return "Invalid hexadecimal color.";
      }
      return true;
    },
  },
  {
    name: "iconWidth",
    type: "number",
    message: "The desired icon width (in dp - we recommend approximately ~100)",
    initial: 100,
    min: 1,
    max: 1000,
  },
  {
    name: "confirmation",
    type: "confirm",
    message:
      "Are you sure? All the existing splashanimation images will be overwritten!",
    initial: true,
  },
];

async function generate({
  projectPath,
  assetsPath,
  iconPath,
  backgroundColor,
  iconWidth: w1,
  confirmation,
}) {
  if (!projectPath || !assetsPath || !iconPath || !w1 || !confirmation) {
    process.exit(1);
  }

  const image = await jimp.read(iconPath);
  const imageMap = [];

  const fullHexadecimal = toFullHexadecimal(backgroundColor);

  const h = (size) =>
    Math.ceil(size * (image.bitmap.height / image.bitmap.width));

  const w15 = w1 * 1.5;
  const w2 = w1 * 2;
  const w3 = w1 * 3;
  const w4 = w1 * 4;

  const androidResPath = path.join(
    projectPath,
    "android",
    "app",
    "src",
    "main",
    "res",
  );

  if (fs.existsSync(androidResPath)) {
    const fileName = `${logoFileName}.png`;

    imageMap.push(
      [path.join(androidResPath, "mipmap-mdpi", fileName), [w1, h(w1)]],
      [path.join(androidResPath, "mipmap-hdpi", fileName), [w15, h(w15)]],
      [path.join(androidResPath, "mipmap-xhdpi", fileName), [w2, h(w2)]],
      [path.join(androidResPath, "mipmap-xxhdpi", fileName), [w3, h(w3)]],
      [path.join(androidResPath, "mipmap-xxxhdpi", fileName), [w4, h(w4)]],
    );
  } else {
    log(`No ${androidResPath} directory found. Skipping android generation…`);
  }

  log("👍  Looking good! Generating files…");

  await Promise.all(
    imageMap.map(([path, [width, height]]) =>
      image
        .clone()
        .cover(width, height)
        .writeAsync(path)
        .then(() => {
          log(`✨  ${path} (${width}x${height})`, true);
        }),
    ),
  );

  if (fs.existsSync(androidResPath)) {
    const drawableDir = path.join(androidResPath, "drawable");
    ensureDir(drawableDir);
    const drawable = path.join(drawableDir, "splashanimation.xml");
    fs.writeFileSync(drawable, drawableXml, "utf-8");

    log(`✨  ${drawable}`, true);

    const valuesDir = path.join(androidResPath, "values");
    ensureDir(valuesDir);
    const colors = path.join(valuesDir, "colors.xml");

    if (fs.existsSync(colors)) {
      const content = fs.readFileSync(colors, "utf-8");

      if (content.match(androidColorRegex)) {
        fs.writeFileSync(
          colors,
          content.replace(
            androidColorRegex,
            `<color name="splash_animation_background">${fullHexadecimal}</color>`,
          ),
          "utf-8",
        );
      } else {
        fs.writeFileSync(
          colors,
          content.replace(
            /<\/resources>/g,
            `    <color name="splash_animation_background">${fullHexadecimal}</color>\n</resources>`,
          ),
          "utf-8",
        );
      }

      log(`✏️   Editing ${colors}`, true);
    } else {
      fs.writeFileSync(
        colors,
        `<resources>\n    <color name="splash_animation_background">${fullHexadecimal}</color>\n</resources>\n`,
        "utf-8",
      );

      log(`✨  ${colors}`, true);
    }
  }

  log(
    `✅  Done! Thanks for using ${chalk.underline("react-native-splash-animations")}.`,
  );
}

prompts(questions)
  .then(generate)
  .catch((error) => log(chalk.red.bold(error.toString())));