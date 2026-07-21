import Link from "next/link";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../../../shared/components/ui/card";
import { Euro, CheckCircle2, XCircle, Eye } from "lucide-react";

interface Props {
  readonly eligibleCount: number;
  readonly notEligibleCount: number;
  readonly totalAmount: number;
}

export function BenefitsCard({
  eligibleCount,
  notEligibleCount,
  totalAmount,
}: Props) {
  return (
    <Card className="h-full">
      <CardHeader className="flex flex-row items-center justify-between pb-4">
  <div className="flex items-center gap-2">
    <Euro className="h-5 w-5 text-green-600" />
    <CardTitle className="text-lg font-bold">
      Benefits
    </CardTitle>
  </div>

  <Link
    href="/benefits"
    className="rounded-md p-1 hover:bg-muted transition-colors"
  >
    <Eye className="h-5 w-5 text-primary" />
  </Link>
</CardHeader>
      <CardContent className="space-y-3 text-sm">
        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <CheckCircle2 className="h-4 w-4 text-green-600" />
            Eligible
          </span>
          <span className="font-semibold">{eligibleCount}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <XCircle className="h-4 w-4 text-red-500" />
            Not eligible
          </span>
          <span className="font-semibold">{notEligibleCount}</span>
        </div>

        <hr />

        <div className="flex items-center justify-between">
          <span className="font-medium">💶 Potential amount</span>
          <span className="font-bold text-green-700">
            {totalAmount.toLocaleString("fr-FR")} €
          </span>
        </div>
      </CardContent>
    </Card>
  );
}