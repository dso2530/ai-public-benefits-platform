import Link from 'next/link';
import { Button } from '../../../shared/components/ui/button';
import { ProfileCard } from '../../profile/component/profile-card';
import { LogoutButton } from '../../auth/components/logout-button';

export function QuickActions() {
  return (
    <div className="flex flex-wrap gap-4">
      <Button asChild>
        <Link href="/documents">
          Upload Documents
        </Link>
      </Button>

      <Button asChild variant="secondary">
        <Link href="/benefits">
          View Benefits
        </Link>
      </Button>

      <Button asChild variant="outline">
        <Link href="/profile">
          Edit Profile
        </Link>
      </Button>

      <LogoutButton />

      <ProfileCard />
    </div>
  );
}