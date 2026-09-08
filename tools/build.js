// Build script: book/*.md (Markdown, per manifest.json) -> dist/*.html
// Renders Mermaid diagrams client-side (with pan/zoom controls) and highlights code with highlight.js.
"use strict";

const fs = require("fs");
const path = require("path");
const MarkdownIt = require("markdown-it");

const ROOT = path.resolve(__dirname, "..");
const BOOK_DIR = path.join(ROOT, "book");
const DIST_DIR = path.join(ROOT, "dist");
const MANIFEST = JSON.parse(fs.readFileSync(path.join(BOOK_DIR, "manifest.json"), "utf8"));

const md = new MarkdownIt({ html: true, linkify: true, typographer: false });

// Render ```mermaid fences as <pre class="mermaid"> for client-side rendering; everything else
// falls through to the default fence renderer (highlight.js highlights it client-side).
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

// Flat, ordered list of chapters with part context.
const flatChapters = [];
for (const part of MANIFEST.parts) {
  for (const ch of part.chapters) {
    flatChapters.push({ ...ch, partId: part.id, partTitle: part.title });
  }
}

function chapterOutPath(ch) {
  return path.join(DIST_DIR, ch.partId, `${ch.slug}.html`);
}

function chapterMdPath(ch) {
  return path.join(BOOK_DIR, ch.partId, `${ch.slug}.md`);
}

function relLink(fromFile, toFile) {
  let rel = path.relative(path.dirname(fromFile), toFile).split(path.sep).join("/");
  if (!rel.startsWith(".")) rel = "./" + rel;
  return rel;
}

function renderSidebar(currentSlug, outFile) {
  let html = `<nav class="sidebar"><div class="sidebar-title"><a href="${relLink(outFile, path.join(DIST_DIR, "index.html"))}">${md.utils.escapeHtml(MANIFEST.title)}</a></div>`;
  for (const part of MANIFEST.parts) {
    html += `<div class="sidebar-part"><div class="sidebar-part-title">${md.utils.escapeHtml(part.title)}</div><ul>`;
    for (const ch of part.chapters) {
      const has = fs.existsSync(chapterMdPath({ ...ch, partId: part.id }));
      const active = ch.slug === currentSlug ? " active" : "";
      const cls = (has ? "" : "planned") + active;
      const href = relLink(outFile, chapterOutPath({ ...ch, partId: part.id }));
      html += `<li class="${cls.trim()}"><a href="${href}">${ch.num}. ${md.utils.escapeHtml(ch.title)}</a></li>`;
    }
    html += `</ul></div>`;
  }
  html += `</nav>`;
  return html;
}

function pageShell({ title, bodyHtml, sidebarHtml, cssHref, prevNext }) {
  return `<!doctype html>
<html lang="vi">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${md.utils.escapeHtml(title)}</title>
<link rel="stylesheet" href="${cssHref}">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/github-dark.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/highlight.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/mermaid/10.9.1/mermaid.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/svg-pan-zoom@3.6.1/dist/svg-pan-zoom.min.js"></script>
</head>
<body>
<button class="nav-toggle" id="navToggle" aria-label="Mở/đóng mục lục">☰</button>
${sidebarHtml}
<main class="content">
<article>
${bodyHtml}
</article>
${prevNext}
</main>
<script>
document.getElementById('navToggle').addEventListener('click', function () {
  document.querySelector('.sidebar').classList.toggle('open');
});

if (window.hljs) { hljs.highlightAll(); }

if (window.mermaid) {
  mermaid.initialize({ startOnLoad: false, theme: 'default', securityLevel: 'loose' });
  mermaid.run({ querySelector: '.mermaid' }).then(function () {
    document.querySelectorAll('.mermaid-wrap').forEach(function (wrap) {
      var svg = wrap.querySelector('svg');
      if (!svg || !window.svgPanZoom) return;
      // Give the SVG concrete pixel dimensions (not "100%") before handing it to
      // svg-pan-zoom: percentage sizing lets the browser's own preserveAspectRatio
      // scaling stack on top of svg-pan-zoom's transform, which throws its fit/center
      // math off by the extra scale factor.
      svg.style.maxWidth = 'none';
      svg.setAttribute('width', wrap.clientWidth);
      svg.setAttribute('height', wrap.clientHeight);
      var pz = svgPanZoom(svg, {
        zoomEnabled: true,
        controlIconsEnabled: true,
        fit: true,
        center: true,
        minZoom: 0.3,
        maxZoom: 10
      });
    });
  });
}
</script>
</body>
</html>
`;
}

// Give every rendered <pre class="mermaid">...</pre> diagram a fixed-height, bordered viewport
// (a wrapping <div class="mermaid-wrap">) so svg-pan-zoom has room to work in.
function wrapMermaidDiagrams(bodyHtml) {
  const OPEN = '<pre class="mermaid">';
  const parts = bodyHtml.split(OPEN);
  let out = parts[0];
  for (let i = 1; i < parts.length; i++) {
    const seg = parts[i];
    const closeIdx = seg.indexOf("</pre>");
    out += '<div class="mermaid-wrap">' + OPEN + seg.slice(0, closeIdx + 6) + "</div>" + seg.slice(closeIdx + 6);
  }
  return out;
}

function buildChapterPage(ch, index) {
  const mdPath = chapterMdPath(ch);
  const outFile = chapterOutPath(ch);
  const cssHref = relLink(outFile, path.join(DIST_DIR, "assets", "style.css"));
  let bodyHtml;
  if (fs.existsSync(mdPath)) {
    const src = fs.readFileSync(mdPath, "utf8");
    bodyHtml = md.render(src);
    bodyHtml = wrapMermaidDiagrams(bodyHtml);
  } else {
    bodyHtml = `<h1>${ch.num}. ${md.utils.escapeHtml(ch.title)}</h1><p class="tbd">Chương này đang được biên soạn.</p>`;
  }
  const prev = flatChapters[index - 1];
  const next = flatChapters[index + 1];
  let prevNext = `<div class="prev-next">`;
  prevNext += prev ? `<a class="prev" href="${relLink(outFile, chapterOutPath(prev))}">&larr; ${prev.num}. ${md.utils.escapeHtml(prev.title)}</a>` : `<span></span>`;
  prevNext += next ? `<a class="next" href="${relLink(outFile, chapterOutPath(next))}">${next.num}. ${md.utils.escapeHtml(next.title)} &rarr;</a>` : `<span></span>`;
  prevNext += `</div>`;

  const html = pageShell({
    title: `${ch.num}. ${ch.title} — ${MANIFEST.title}`,
    bodyHtml,
    sidebarHtml: renderSidebar(ch.slug, outFile),
    cssHref,
    prevNext
  });
  fs.mkdirSync(path.dirname(outFile), { recursive: true });
  fs.writeFileSync(outFile, html, "utf8");
}

function buildIndex() {
  const outFile = path.join(DIST_DIR, "index.html");
  const cssHref = relLink(outFile, path.join(DIST_DIR, "assets", "style.css"));
  let toc = `<h1>${md.utils.escapeHtml(MANIFEST.title)}</h1><p class="subtitle">${md.utils.escapeHtml(MANIFEST.subtitle)}</p>`;
  for (const part of MANIFEST.parts) {
    toc += `<h2>${md.utils.escapeHtml(part.title)}</h2><ul>`;
    for (const ch of part.chapters) {
      const has = fs.existsSync(chapterMdPath({ ...ch, partId: part.id }));
      const href = relLink(outFile, chapterOutPath({ ...ch, partId: part.id }));
      toc += `<li class="${has ? "" : "planned"}"><a href="${href}">${ch.num}. ${md.utils.escapeHtml(ch.title)}</a>${has ? "" : ' <span class="badge">sắp có</span>'}</li>`;
    }
    toc += `</ul>`;
  }
  const html = pageShell({
    title: MANIFEST.title,
    bodyHtml: toc,
    sidebarHtml: renderSidebar(null, outFile),
    cssHref,
    prevNext: ""
  });
  fs.writeFileSync(outFile, html, "utf8");
}

function copyAssets() {
  const cssSrc = path.join(__dirname, "style.css");
  const cssOut = path.join(DIST_DIR, "assets", "style.css");
  fs.mkdirSync(path.dirname(cssOut), { recursive: true });
  fs.copyFileSync(cssSrc, cssOut);
}

function main() {
  fs.rmSync(DIST_DIR, { recursive: true, force: true });
  copyAssets();
  flatChapters.forEach(buildChapterPage);
  buildIndex();
  console.log(`Đã build ${flatChapters.length} chương vào ${path.relative(ROOT, DIST_DIR)}/`);
}

main();
