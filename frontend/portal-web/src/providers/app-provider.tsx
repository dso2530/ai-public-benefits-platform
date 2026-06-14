"use client";

import { QueryProvider } from "./query-provider";

export function AppProvider({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <QueryProvider>
      {children}
    </QueryProvider>
  );
}