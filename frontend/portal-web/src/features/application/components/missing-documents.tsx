"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

import { DocumentType } from "../types/document-type";
import { documentLabels } from "../constants/document-labels";
import { uploadApplicationDocument } from "../services/application.service";

type Props = {
    readonly applicationId: string;
    readonly missingDocuments: DocumentType[];
};

export function MissingDocuments({
    applicationId,
    missingDocuments,
}: Props) {
    const router = useRouter();
    const documents = missingDocuments ?? [];

    const [files, setFiles] = useState<Partial<Record<DocumentType, File>>>({});
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleFileChange = (documentType: DocumentType, file?: File) => {
        if (!file) {
            return;
        }

        setFiles((current) => ({
            ...current,
            [documentType]: file,
        }));
    };

    const handleUpload = async () => {
        try {
            setLoading(true);
            setError(null);

            await Promise.all(
                Object.entries(files).map(([documentType, file]) =>
                    uploadApplicationDocument(applicationId, documentType as DocumentType, file)
                )
            );

            router.push(`/applications/${applicationId}`);
        } catch (err) {
            console.error("Erreur upload documents", err);
            setError("Une erreur est survenue lors de l'envoi des documents. Veuillez réessayer.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="rounded-xl border p-6 space-y-6">
            <div>
                <h1 className="text-xl font-semibold">
                    Votre dossier est presque terminé
                </h1>
                <p className="mt-2 text-sm text-muted-foreground">
                    Il manque encore les pièces justificatives suivantes :
                </p>
            </div>

            <ul className="space-y-4">
                {documents.map((document) => (
                    <li
                        key={document}
                        className="flex items-center justify-between rounded-lg border p-4"
                    >
                        <span>
                            {files[document] ? "☑" : "☐"} {documentLabels[document]}
                        </span>

                        <input
                            type="file"
                            onChange={(event) =>
                                handleFileChange(document, event.target.files?.[0])
                            }
                        />
                    </li>
                ))}
            </ul>

            {error && (
                <p className="text-sm text-red-600" role="alert">
                    {error}
                </p>
            )}

            <button
                type="button"
                onClick={handleUpload}
                disabled={loading || Object.keys(files).length === 0}
                className="rounded bg-primary px-4 py-2 text-white disabled:opacity-50"
            >
                {loading ? "Envoi en cours..." : "Ajouter les documents"}
            </button>
        </div>
    );
}