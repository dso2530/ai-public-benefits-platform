

import { Badge } from "../../../shared/components/ui/badge";

type Props = {
    readonly status: string;
};

const variants: Record<
    string,
    { label: string; className: string }
> = {
    GENERATED: {
        label: "Générée",
        className: "bg-blue-100 text-blue-800",
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
    const variant = variants[status] ?? {
        label: status,
        className: "bg-gray-100 text-gray-800",
    };

    return (
        <Badge className={variant.className}>
            {variant.label}
        </Badge>
    );
}