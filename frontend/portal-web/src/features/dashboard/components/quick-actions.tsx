import Link from 'next/link';
import { Button } from '../../../shared/components/ui/button';

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
    </div>
  );
}