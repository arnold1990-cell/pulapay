import type { ReactNode } from 'react';
import { cn } from '../../lib/utils';

type CardProps = {
  children: ReactNode;
  className?: string;
};

export default function Card({ children, className }: CardProps) {
  return <section className={cn('card', className)}>{children}</section>;
}
