// Build script: book/*.md -> dist/android-tu-co-ban-den-nang-cao.pdf
// Gộp toàn bộ chương thành MỘT trang HTML dài (bìa + mục lục + từng chương ngắt
// trang), dùng Puppeteer (Chromium headless) render Mermaid/highlight.js rồi in
// ra PDF — không cần Pandoc/LaTeX, tái sử dụng đúng nguồn Markdown như bản HTML.
"use strict";

const fs = require("fs");
const path = require("path");
const puppeteer = require("puppeteer");
const MarkdownIt = require("markdown-it");

const ROOT = path.resolve(__dirname, "..");
const BOOK_DIR = path.join(ROOT, "book");
const DIST_DIR = path.join(ROOT, "dist");
const MANIFEST = JSON.parse(fs.readFileSync(path.join(BOOK_DIR, "manifest.json"), "utf8"));
const PDF_PATH = path.join(DIST_DIR, "android-tu-co-ban-den-nang-cao.pdf");

const md = new MarkdownIt({ html: true, linkify: true, typographer: false });

// Cùng cách xử lý fence ```mermaid như tools/build.js — giữ nhất quán giữa bản
// web và bản in, dù đây là bản dựng riêng cho PDF.
const defaultFence = md.renderer.rules.fence || function (tokens, idx, options, env, self) {
  return self.renderToken(tokens, idx, options);
};
md.renderer.rules.fence = function (tokens, idx, options, env, self) {
  const token = tokens[idx];
  const lang = (token.info || "").trim().split(/\s+/)[0];
  if (lang === "mermaid") {
    return `<pre class="mermaid">\n${md.utils.escapeHtml(token.content)}</pre>\n`;
  }
  return defaultFence(tokens, idx, options, env, self);
};

function chapterMdPath(partId, slug) {
  return path.join(BOOK_DIR, partId, `${slug}.md`);
}

function numberedTitle(ch) {
  return ch.label ? `${ch.label} — ${ch.title}` : `Chương ${ch.num}. ${ch.title}`;
}

function buildCoverHtml() {
  return `<section class="pdf-cover">
    <h1>${md.utils.escapeHtml(MANIFEST.title)}</h1>
    <p class="pdf-subtitle">${md.utils.escapeHtml(MANIFEST.subtitle)}</p>
  </section>`;
}

function buildTocHtml() {
  let html = `<section class="pdf-toc"><h1>Mục lục</h1>`;
  for (const part of MANIFEST.parts) {
    html += `<h2>${md.utils.escapeHtml(part.title)}</h2><ul>`;
    for (const ch of part.chapters) {
      html += `<li><a href="#${ch.slug}">${md.utils.escapeHtml(numberedTitle(ch))}</a></li>`;
    }
    html += `</ul>`;
  }
  html += `</section>`;
  return html;
}

function buildChaptersHtml() {
  let html = "";
  let missing = [];
  for (const part of MANIFEST.parts) {
    html += `<section class="pdf-part-divider"><h1>${md.utils.escapeHtml(part.title)}</h1></section>`;
    for (const ch of part.chapters) {
      const mdPath = chapterMdPath(part.id, ch.slug);
      if (!fs.existsSync(mdPath)) {
        missing.push(ch.slug);
        continue;
      }
      const src = fs.readFileSync(mdPath, "utf8");
      const body = md.render(src);
      html += `<section class="pdf-chapter" id="${ch.slug}">${body}</section>`;
    }
  }
  if (missing.length > 0) {
    console.log(`(bỏ qua ${missing.length} chương chưa có file: ${missing.join(", ")})`);
  }
  return html;
}

function buildFullHtml() {
  const css = fs.readFileSync(path.join(__dirname, "pdf-style.css"), "utf8");
  return `<!doctype html>
<html lang="vi">
<head>
<meta charset="utf-8">
<title>${md.utils.escapeHtml(MANIFEST.title)}</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/github.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/highlight.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/mermaid/10.9.1/mermaid.min.js"></script>
<style>${css}</style>
</head>
<body>
${buildCoverHtml()}
${buildTocHtml()}
${buildChaptersHtml()}
</body>
</html>`;
}

async function main() {
  const html = buildFullHtml();
  const tmpDir = path.join(ROOT, ".pdf-build-tmp");
  fs.mkdirSync(tmpDir, { recursive: true });
  const tmpHtmlPath = path.join(tmpDir, "print.html");
  fs.writeFileSync(tmpHtmlPath, html, "utf8");

  const browser = await puppeteer.launch();
  try {
    const page = await browser.newPage();
    page.setDefaultTimeout(120000);
    await page.goto("file:///" + tmpHtmlPath.replace(/\\/g, "/"), {
      waitUntil: "networkidle0",
      timeout: 120000,
    });

    await page.evaluate(() => {
      if (window.hljs) window.hljs.highlightAll();
    });

    await page.evaluate(async () => {
      if (window.mermaid) {
        window.mermaid.initialize({ startOnLoad: false, theme: "default", securityLevel: "loose" });
        await window.mermaid.run({ querySelector: ".mermaid" });
      }
    });

    // Chờ layout ổn định sau khi Mermaid vừa chèn SVG vào DOM (kích thước thật
    // của SVG chỉ chốt sau một vài khung hình, giống lý do đã gặp ở build.js).
    await new Promise((resolve) => setTimeout(resolve, 1500));

    fs.mkdirSync(DIST_DIR, { recursive: true });
    await page.pdf({
      path: PDF_PATH,
      format: "A4",
      printBackground: true,
      margin: { top: "18mm", bottom: "16mm", left: "16mm", right: "16mm" },
      displayHeaderFooter: true,
      headerTemplate: "<div></div>",
      footerTemplate: `<div style="font-size:8px; width:100%; text-align:center; color:#999;">
        <span class="pageNumber"></span> / <span class="totalPages"></span>
      </div>`,
    });
  } finally {
    await browser.close();
    fs.rmSync(tmpDir, { recursive: true, force: true });
  }

  const sizeMb = (fs.statSync(PDF_PATH).size / (1024 * 1024)).toFixed(1);
  console.log(`Đã tạo PDF: ${path.relative(ROOT, PDF_PATH)} (${sizeMb} MB)`);
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
