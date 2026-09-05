import { Badge } from "../../../shared/components/ui/badge";
import { Application } from "../types/application";

type Props = {
  readonly status: Application["status"];
};

const variants: Record<
  Application["status"],
  { label: string; className: string }
> = {
  GENERATED: {
    label: "Générée",
    className: "bg-blue-100 text-blue-800",
  },
  READY_TO_COMPLETE: {
    label: "À finaliser",
    className: "bg-orange-100 text-orange-800",
  },
  READY_TO_SUBMIT: {
    label: "Prête à envoyer",
    className: "bg-purple-100 text-purple-800",
  },
  SUBMITTED: {
    label: "Soumise",
    className: "bg-yellow-100 text-yellow-800",
  },
  ACCEPTED: {
    label: "Acceptée",
    className: "bg-green-100 text-green-800",
  },
  REJECTED: {
    label: "Refusée",
    className: "bg-red-100 text-red-800",
  },
};

export function ApplicationStatusBadge({ status }: Props) {
  const variant = variants[status];

  return <Badge className={variant.className}>{variant.label}</Badge>;
}
