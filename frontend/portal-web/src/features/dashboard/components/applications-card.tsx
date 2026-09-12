// features/dashboard/components/applications-card.tsx

import {
  CheckCircle2,
  Clock3,
  Eye,
  FileText,
  Send,
  XCircle,
} from "lucide-react";
import Link from "next/link";

type Props = {
  readonly total: number;
  readonly generated: number;
  readonly readyToSubmit: number;
  readonly submitted: number;
  readonly accepted: number;
  readonly rejected: number;
};

export function ApplicationsCard({
  total,
  generated,
  readyToSubmit,
  submitted,
  accepted,
  rejected,
}: Props) {
  return (
    <div className="rounded-xl border bg-white p-6 shadow-sm">
      <div className="mb-4 flex items-center justify-between">
        <h2 className="text-xl font-semibold">📝 Applications</h2>

        <Link
          href="/applications"
          className="text-sm font-medium text-blue-600 hover:underline"
        >
          <Eye className="h-5 w-5 text-primary" />
        </Link>
      </div>

      <div className="space-y-3 text-sm">
        <div className="flex items-center justify-between font-semibold">
          <span>Total</span>
          <span>{total ?? 0}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <FileText className="h-4 w-4 text-amber-500" />À compléter
          </span>
          <span>{generated ?? 0}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <Send className="h-4 w-4 text-blue-600" />
            Prêtes à envoyer
          </span>
          <span>{readyToSubmit ?? 0}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <Clock3 className="h-4 w-4 text-amber-500" />
            Envoyées
          </span>
          <span>{submitted ?? 0}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <CheckCircle2 className="h-4 w-4 text-green-600" />
            Acceptées
          </span>
          <span>{accepted ?? 0}</span>
        </div>

        <div className="flex items-center justify-between">
          <span className="flex items-center gap-2">
            <XCircle className="h-4 w-4 text-red-600" />
            Refusées
          </span>
          <span>{rejected ?? 0}</span>
        </div>
      </div>
    </div>
  );
}
