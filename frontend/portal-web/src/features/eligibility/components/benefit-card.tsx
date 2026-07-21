import Link from "next/link";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../../../shared/components/ui/card";
import { Button } from "../../../shared/components/ui/button";
import { CheckCircle2, XCircle, Euro } from "lucide-react";

import { Eligibility } from "../types/eligibility";

interface BenefitCardProps {
  readonly benefit: Eligibility;
}

export function BenefitCard({ benefit }: BenefitCardProps) {
  const eligible = benefit.status === "ELIGIBLE";

  return (
    <Card className="h-full rounded-xl shadow-sm transition-shadow hover:shadow-md">
      <CardHeader className="space-y-3">
        <div className="flex items-center justify-between">
          <CardTitle className="text-lg font-bold">
            ⚡ {benefit.aidName}
          </CardTitle>

          <span
            className={`flex items-center gap-1 rounded-full px-3 py-1 text-xs font-semibold ${eligible
              ? "bg-green-100 text-green-700"
              : "bg-red-100 text-red-700"
              }`}
          >
            {eligible ? (
              <CheckCircle2 className="h-3 w-3" />
            ) : (
              <XCircle className="h-3 w-3" />
            )}
            {eligible ? "Éligible" : "Non éligible"}
          </span>
        </div>

        {benefit.description && (
          <p className="text-sm text-muted-foreground">
            {benefit.description}
          </p>
        )}
      </CardHeader>

      <CardContent className="space-y-5">
        <div>
          <p className="flex items-center gap-1 text-xs text-muted-foreground">
            <Euro className="h-3 w-3" />
            Montant estimé
          </p>

          <p className="text-2xl font-bold text-primary">
            {benefit.estimatedAmountLabel ?? "-"}
          </p>
        </div>

        {!eligible && benefit.reason && (
          <p className="text-sm text-muted-foreground">
            {benefit.reason}
          </p>
        )}

        {eligible && benefit.applicationId ? (
          <Button asChild className="w-full">
            <Link href={`/applications/${benefit.applicationId}`}>
              {benefit.actionLabel ?? "Finaliser ma demande"}
            </Link>
          </Button>
        ) : (
          <Button variant="outline" className="w-full" disabled>
            {benefit.actionLabel ?? "Non éligible"}
          </Button>
        )}
      </CardContent>
    </Card>
  );
}