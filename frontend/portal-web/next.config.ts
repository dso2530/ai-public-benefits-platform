import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: "http://localhost:8084/api/:path*",
      },
    ];
  },
  experimental: {
    serverActions: {
      bodySizeLimit: '10mb', // Autorise l'envoi de gros PDFs d'avis d'imposition
    },
  },
  // Headers de sécurité stricts (Protection contre les failles XSS / Clickjacking)
  async headers() {
    return [
      {
        source: '/(.*)',
        headers: [
          { key: 'X-Content-Type-Options', value: 'nosniff' },
          { key: 'X-Frame-Options', value: 'DENY' },
          { key: 'X-XSS-Protection', value: '1; mode=block' },
          { key: 'Referrer-Policy', value: 'strict-origin-when-cross-origin' },
        ],
      },
    ];
  },
};

export default nextConfig;
