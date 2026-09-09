// Kiểm tra độ dài các file mô tả cửa hàng ĐÚNG giới hạn ký tự thật của Google
// Play Console — tự chạy trước khi thật sự đăng nhập Console để tránh mất thời
// gian dán rồi bị báo lỗi "quá dài" ngay trên form. Chạy: node tools/validate-store-listing.js
"use strict";

const fs = require("fs");
const path = require("path");

const ROOT = path.resolve(__dirname, "..");

// Giới hạn ký tự CHÍNH THỨC của Google Play Console (2026) — nếu Google thay
// đổi giới hạn trong tương lai, cập nhật lại các con số này.
const LIMITS = {
  "store-listing/*/title.txt": 30,
  "store-listing/*/short_description.txt": 80,
  "store-listing/*/full_description.txt": 4000,
  "release-notes/*/default.txt": 500,
};

function findLocaleDirs(baseDir) {
  if (!fs.existsSync(baseDir)) return [];
  return fs.readdirSync(baseDir, { withFileTypes: true })
    .filter((e) => e.isDirectory())
    .map((e) => e.name);
}

function checkFile(filePath, limit) {
  if (!fs.existsSync(filePath)) {
    return { status: "MISSING", length: 0 };
  }
  const content = fs.readFileSync(filePath, "utf8").replace(/\r\n/g, "\n").trimEnd();
  const length = [...content].length; // đếm theo ký tự Unicode, không theo byte UTF-8
  return { status: length <= limit ? "OK" : "QUÁ DÀI", length };
}

function main() {
  let hasFailure = false;

  for (const [pattern, limit] of Object.entries(LIMITS)) {
    const [baseFolder, , fileName] = pattern.split("/");
    const baseDir = path.join(ROOT, baseFolder);
    const locales = findLocaleDirs(baseDir);

    if (locales.length === 0) {
      console.log(`(bỏ qua — không có thư mục nào trong ${baseFolder}/)`);
      continue;
    }

    for (const locale of locales) {
      const filePath = path.join(baseDir, locale, fileName);
      const result = checkFile(filePath, limit);
      const label = `${baseFolder}/${locale}/${fileName}`;
      if (result.status === "OK") {
        console.log(`OK       ${label} (${result.length}/${limit} ký tự)`);
      } else if (result.status === "MISSING") {
        console.log(`THIẾU    ${label}`);
        hasFailure = true;
      } else {
        console.log(`QUÁ DÀI  ${label} (${result.length}/${limit} ký tự)`);
        hasFailure = true;
      }
    }
  }

  if (hasFailure) {
    console.log("\nCó ít nhất một mục chưa hợp lệ — sửa trước khi dán vào Play Console.");
    process.exit(1);
  } else {
    console.log("\nTất cả mục đều hợp lệ.");
  }
}

main();
