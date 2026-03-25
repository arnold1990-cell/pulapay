import type { InputHTMLAttributes } from 'react';
import { cn } from '../../lib/utils';

type InputProps = InputHTMLAttributes<HTMLInputElement> & {
  label?: string;
  error?: string;
};

export default function Input({ className, label, error, id, ...props }: InputProps) {
  const inputId = id ?? props.name;

  return (
    <div className="form-group">
      {label ? (
        <label htmlFor={inputId} className="form-label">
          {label}
        </label>
      ) : null}
      <input id={inputId} className={cn('input', error && 'input-error', className)} {...props} />
      {error ? <p className="form-error">{error}</p> : null}
    </div>
  );
}
