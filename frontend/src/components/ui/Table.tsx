import type { ReactNode } from 'react';

type Column<T> = {
  header: string;
  cell: (row: T) => ReactNode;
};

type TableProps<T> = {
  columns: Array<Column<T>>;
  data: T[];
  emptyMessage?: string;
};

export default function Table<T>({ columns, data, emptyMessage = 'No records found.' }: TableProps<T>) {
  return (
    <div className="table-wrapper">
      <table className="table">
        <thead>
          <tr>
            {columns.map((column) => (
              <th key={column.header}>{column.header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.length === 0 ? (
            <tr>
              <td colSpan={columns.length} className="table-empty">
                {emptyMessage}
              </td>
            </tr>
          ) : (
            data.map((row, rowIndex) => (
              <tr key={rowIndex}>
                {columns.map((column) => (
                  <td key={column.header}>{column.cell(row)}</td>
                ))}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
