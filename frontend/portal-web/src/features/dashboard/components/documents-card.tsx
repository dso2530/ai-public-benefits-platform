import Link from "next/link";
import {
  FileText,
  CheckCircle2,
  Clock3,
  ArrowRight,
  Eye
} from "lucide-react";

import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../../../shared/components/ui/card";

interface Props {
  readonly total: number;
  readonly validated: number;
  readonly pending: number;
}

export function DocumentsCard({
  total,
  validated,
  pending,
}: Props) {
  return (
    <Card className="h-full">
      <CardHeader className="flex flex-row items-center justify-between pb-2">
        <CardTitle className="text-lg font-bold">
          📄 Documents
        </CardTitle>

        <Link
          href="/documents"
          className="flex items-center gap-1 text-sm text-primary hover:underline"
        >
          <Eye className="h-5 w-5 text-primary" />
        </Link>
      </CardHeader>

      <CardContent className="space-y-3 text-sm">
        <div className="flex items-center justify-between font-semibold">
          <span>Total</span>
          <span>{total}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <Clock3 className="h-4 w-4 text-amber-500" />
            En attente
          </span>
          <span>{pending}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <CheckCircle2 className="h-4 w-4 text-green-600" />
            Validés
          </span>
          <span>{validated}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <FileText className="h-4 w-4 text-blue-600" />
            Restants
          </span>
          <span>{Math.max(total - validated, 0)}</span>
        </div>
      </CardContent>
    </Card>
  );
}