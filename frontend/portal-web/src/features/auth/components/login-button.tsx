"use client";

export function LoginButton() {

  const login = () => {

   window.location.href = "http://localhost:8084/oauth2/authorization/keycloak";
  };

  return (
    <button
      onClick={login}
      className="rounded bg-blue-600 px-4 py-2 text-white"
    >
      Se connecter
    </button>
  );
}