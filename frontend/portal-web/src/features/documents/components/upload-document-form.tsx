"use client";

import { useState } from "react";
import { UploadCloud, Loader2 } from "lucide-react";

import { Card, CardContent, CardHeader, CardTitle } from '../../../shared/components/ui/card';


import { Button } from "../../../shared/components/ui/button";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../../../shared/components/ui/select";

interface Props {
  readonly onUpload: (
    file: File,
    documentType: string
  ) => Promise<void>;

  readonly loading?: boolean;
}

export function UploadDocumentForm({
  onUpload,
  loading = false,
}: Props) {
  const [file, setFile] =
    useState<File | null>(null);

  const [documentType, setDocumentType] =
    useState("");

  const handleSubmit = async (
    e: React.SubmitEvent
  ) => {
    e.preventDefault();

    if (!file || !documentType) {
      return;
    }

    await onUpload(
      file,
      documentType
    );

    setFile(null);
    setDocumentType("");
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>
          Ajouter un document
        </CardTitle>
      </CardHeader>

      <CardContent>
        <form
          onSubmit={handleSubmit}
          className="space-y-6"
        >
          <Select
            value={documentType}
            onValueChange={
              setDocumentType
            }
          >
            <SelectTrigger>
              <SelectValue placeholder="Type de document" />
            </SelectTrigger>

            <SelectContent>
              <SelectItem value="IDENTITY_CARD">
                Carte d'identité
              </SelectItem>

              <SelectItem value="PROOF_OF_ADDRESS">
                Justificatif de domicile
              </SelectItem>

              <SelectItem value="TAX_NOTICE">
                Avis d'imposition
              </SelectItem>
            </SelectContent>
          </Select>

          <label
            className="
              flex flex-col items-center justify-center
              border-2 border-dashed rounded-xl
              p-10 cursor-pointer
              hover:bg-muted/40 transition-colors
            "
          >
            <UploadCloud className="h-10 w-10 mb-4" />

            <p className="font-medium">
              Déposez votre document ici
            </p>

            <p className="text-sm text-muted-foreground">
              PDF, JPG, PNG
            </p>

            {file && (
              <p className="mt-4 text-sm font-medium">
                {file.name}
              </p>
            )}

            <input
              type="file"
              accept=".pdf,.png,.jpg,.jpeg"
              className="hidden"
              onChange={(e) =>
                setFile(
                  e.target.files?.[0] ?? null
                )
              }
            />
          </label>

          <Button
            type="submit"
            className="w-full"
            disabled={
              loading ||
              !file ||
              !documentType
            }
          >
            {loading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Téléversement...
              </>
            ) : (
              "Envoyer le document"
            )}
          </Button>
        </form>
      </CardContent>
    </Card>
  );
}