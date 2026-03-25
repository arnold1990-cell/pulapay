export default function Table({ headers, rows }: { headers: string[]; rows: React.ReactNode[][] }) {
  return <table style={{ width:'100%', borderCollapse:'collapse' }}><thead><tr>{headers.map(h => <th key={h} style={{ textAlign:'left', borderBottom:'1px solid #eee', padding:'0.5rem' }}>{h}</th>)}</tr></thead><tbody>{rows.map((r,i)=><tr key={i}>{r.map((c,j)=><td key={j} style={{ padding:'0.5rem', borderBottom:'1px solid #f0f0f0' }}>{c}</td>)}</tr>)}</tbody></table>;
}
