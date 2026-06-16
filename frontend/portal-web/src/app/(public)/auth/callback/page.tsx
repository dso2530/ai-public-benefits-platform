// app/auth/callback/page.tsx

"use client";

import { useEffect } from "react";

import { useRouter } from "next/navigation";

import { tokenStorage }
from "../../../../shared/store/token-storage";

export default function CallbackPage() {

  const router = useRouter();

  useEffect(() => {

    const params =
      new URLSearchParams(
        window.location.search
      );

    const token =
      params.get("token");

    if (!token) {

      router.push("/login");

      return;
    }

    tokenStorage.set(token);

    router.push("/dashboard");

  }, [router]);

  return (
    <div>
      Authentification...
    </div>
  );
}