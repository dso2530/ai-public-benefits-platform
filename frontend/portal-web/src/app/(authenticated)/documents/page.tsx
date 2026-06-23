"use client";

import { UploadDocumentForm } from "../../../features/documents/components/upload-document-form";
import { DocumentCard } from "../../../features/documents/components/document-card";

import { useDocuments } from "../../../features/documents/hooks/useDocuments";
import { useUploadDocument } from "../../../features/documents/hooks/useUploadDocument";

export default function DocumentsPage() {
  const {
    documents,
    loading,
    refresh,
    deleteDocument,
    downloadDocument,
  } = useDocuments();

  const {
    upload,
    loading: uploadLoading,
  } = useUploadDocument(refresh);

  if (loading) {
    return (
      <div className="container mx-auto p-6">
        <p>Chargement des documents...</p>
      </div>
    );
  }

  return (
    <div className="container mx-auto max-w-6xl p-6 space-y-8">
      <div>
        <h1 className="text-3xl font-bold">
          Mes documents
        </h1>

        <p className="text-muted-foreground mt-2">
          Gérez vos justificatifs administratifs.
        </p>
      </div>

      <UploadDocumentForm
        onUpload={upload}
        loading={uploadLoading}
      />

      {documents.length === 0 ? (
        <div className="border rounded-lg p-8 text-center">
          <p className="text-muted-foreground">
            Aucun document disponible.
          </p>
        </div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {documents.map((document) => (
            <DocumentCard
              key={document.id}
              document={document}
              onDelete={deleteDocument}
              onDownload={downloadDocument}
            />
          ))}
        </div>
      )}
    </div>
  );
}