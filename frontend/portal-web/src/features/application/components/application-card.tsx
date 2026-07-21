import { ApplicationStatusBadge } from "./application-status-badge";
import { ApplicationActions } from "./application-actions";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
} from "@/src/shared/components/ui/card";
import { Application } from "../types/application";
import { documentLabels } from "../constants/document-labels";

type Props = {
  readonly application: Application;
  readonly detailed?: boolean;
};

export function ApplicationCard({
  application,
  detailed = false,
}: Props) {
  return (
    <Card className="rounded-xl shadow-sm">
      <CardHeader>
        <div className="flex items-center justify-between">
          <div>
            <CardTitle>{application.aidName}</CardTitle>
            <CardDescription>{application.aidCode}</CardDescription>
          </div>

          <ApplicationStatusBadge status={application.status} />
        </div>
      </CardHeader>

      <CardContent className="space-y-4">
        {detailed && (
          <p className="text-sm text-muted-foreground">
            Cette demande a été générée automatiquement à partir de votre
            éligibilité.
          </p>
        )}

        <div className="text-sm">
          <span className="font-medium">Créée le :</span>{" "}
          {new Date(application.createdAt).toLocaleDateString("fr-FR")}
        </div>

        {application.status === "READY_TO_COMPLETE" &&
          application.missingDocuments.length > 0 && (
            <div className="rounded-lg border border-amber-200 bg-amber-50 p-4">
              <h3 className="font-medium text-amber-900">
                Votre dossier est presque terminé
              </h3>

              <p className="mt-2 text-sm text-amber-800">
                Il manque encore les pièces justificatives suivantes :
              </p>

              <ul className="mt-3 space-y-1 text-sm text-amber-900">
                {application.missingDocuments.map((document) => (
                  <li key={document}>
                    ☐ {documentLabels[document]}
                  </li>
                ))}
              </ul>
            </div>
          )}

        <ApplicationActions
          application={application}

        />
      </CardContent>
    </Card>
  );
}