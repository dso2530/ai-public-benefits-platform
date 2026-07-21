import Link from "next/link";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../../../shared/components/ui/card";

import {
  House,
  MapPin,
  Home,
  Users,
  UserRound,
  User,
  Eye,
} from "lucide-react";

interface Props {
  city: string;
  housingStatus: string;
  children: number;
  singleParent: boolean;
}

export function HouseholdCard({
  city,
  housingStatus,
  children,
  singleParent,
}: Readonly<Props>) {
  return (
    <Card className="h-full">
      <CardHeader className="flex flex-row items-center justify-between">
        <div className="flex items-center gap-2">
          <User className="h-5 w-5 text-blue-600" />
          <CardTitle className="text-lg font-bold">
            Household
          </CardTitle>
        </div>

        <Link
          href="/profile"
          className="rounded-md p-1 hover:bg-muted transition-colors"
        >
          <Eye className="h-5 w-5 text-primary" />
        </Link>
      </CardHeader>

      <CardContent className="space-y-3 text-sm">
        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <MapPin className="h-4 w-4 text-red-500" />
            City
          </span>
          <span className="font-semibold">{city}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <Home className="h-4 w-4 text-amber-500" />
            Housing
          </span>
          <span className="font-semibold">{housingStatus}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <Users className="h-4 w-4 text-green-600" />
            Children
          </span>
          <span className="font-semibold">{children}</span>
        </div>

        <hr />

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <UserRound className="h-4 w-4 text-indigo-600" />
            Family
          </span>
          <span className="font-semibold">
            {singleParent ? "Single Parent" : "Two Parents"}
          </span>
        </div>
      </CardContent>
    </Card>
  );
}