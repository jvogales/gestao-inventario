document.addEventListener('DOMContentLoaded', () => {
  const links = [...document.querySelectorAll('.sidebar nav a')];
  const sections = links.map(link => document.querySelector(link.getAttribute('href'))).filter(Boolean);
  const mark = () => {
    const current = [...sections].reverse().find(section => section.getBoundingClientRect().top <= 120) || sections[0];
    links.forEach(link => link.classList.toggle('active', link.getAttribute('href') === `#${current.id}`));
  };
  mark();
  document.addEventListener('scroll', mark, {passive: true});
  const notice = document.querySelector('.notice');
  if (notice) window.setTimeout(() => notice.remove(), 5000);
});
