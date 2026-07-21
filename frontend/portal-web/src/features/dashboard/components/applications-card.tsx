// features/dashboard/components/applications-card.tsx

import { Eye } from "lucide-react";
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
            <div className="flex items-center justify-between mb-4">
                <h2 className="text-xl font-semibold">📝 Applications</h2>

                <Link
                    href="/applications"
                    className="text-sm font-medium text-blue-600 hover:underline"
                >
                    <Eye className="h-5 w-5 text-primary" />
                </Link>
            </div>

            <div className="space-y-2 text-sm">
                <p>
                    <span className="font-medium">Total :</span> {total}
                </p>

                <p>
                    📝 <span className="font-medium">À compléter :</span> {generated}
                </p>

                <p>
                    📤 <span className="font-medium">Prêtes à envoyer :</span>{" "}
                    {readyToSubmit}
                </p>

                <p>
                    ⏳ <span className="font-medium">Envoyées :</span> {submitted}
                </p>

                <p className="text-green-700">
                    ✅ <span className="font-medium">Acceptées :</span> {accepted}
                </p>

                <p className="text-red-700">
                    ❌ <span className="font-medium">Refusées :</span> {rejected}
                </p>
            </div>
        </div>
    );
}