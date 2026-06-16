"use client";

import { useAuth }
from "../hook/use-auth";
export function LogoutButton() {

  const { logout }
    = useAuth();

  return (

    <button
      onClick={logout}
    >
      Déconnexion
    </button>
  );
}