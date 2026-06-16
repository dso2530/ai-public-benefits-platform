"use client";

import { useAuth } from "../../auth/hook/use-auth";

export function ProfileCard() {

  const { user, loading } = useAuth();

  if (loading) {
    return <div>Chargement du profil...</div>;
  }

  if (!user) {
    return <div>Utilisateur non connecté</div>;
  }

  return (
    <div className="rounded-lg border p-6 shadow-sm bg-white">
      
      <div className="space-y-2">
    
        <p>
          <strong>Name :</strong> {user.name}
        </p>

        <p>
          <strong>Email :</strong> {user.email}
        </p>

       
      </div>
    </div>
  );
}