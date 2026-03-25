export const currency = (value: number | string, symbol = 'BWP') =>
  `${symbol} ${Number(value).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;

export const cn = (...classes: Array<string | false | null | undefined>) => classes.filter(Boolean).join(' ');
