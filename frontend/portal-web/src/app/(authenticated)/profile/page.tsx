import { ProfileCard } from "../../../features/profile/components/profile-card";

export default function ProfilePage() {
  return (
    <div className="container mx-auto p-6">
      <h1 className="text-2xl font-bold mb-6">My  Profile</h1>
      <ProfileCard />
    </div>
  );
}