// app/login/page.tsx

import { LoginButton }
from "../../../features/auth/components/login-button";

export default function LoginPage() {

  return (
    <div className="flex min-h-screen items-center justify-center">

      <LoginButton />

    </div>
  );
}