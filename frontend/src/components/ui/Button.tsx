import type { ButtonHTMLAttributes } from 'react';
export default function Button(props: ButtonHTMLAttributes<HTMLButtonElement>) {
  return <button {...props} style={{ background:'#0f62fe', color:'#fff', border:'none', borderRadius:8, padding:'0.6rem 1rem', cursor:'pointer' }} />;
}
