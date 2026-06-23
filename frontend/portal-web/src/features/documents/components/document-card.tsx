"use client";

import { Card, CardContent, CardHeader, CardTitle } from '../../../shared/components/ui/card';
import { Badge } from "../../../shared/components/ui/badge";
import { Button } from "../../../shared/components/ui/button";
import { Download, Trash2, FileText } from "lucide-react";
import { Document } from '../types/document';



interface DocumentCardProps {
  readonly document: Document;
  readonly onDownload: (id: number) => void;
  readonly onDelete: (id: number) => void;
}

export function DocumentCard({
  document,
  onDownload,
  onDelete,
}: DocumentCardProps) {
  const formatSize = (size?: number) => {
    if (!size) return "-";

    return `${(size / 1024 / 1024).toFixed(2)} MB`;
  };

  const getStatusVariant = () => {
    switch (document.status) {
      case "VALIDATED":
        return "default";

      case "REJECTED":
        return "destructive";

      default:
        return "secondary";
    }
  };

  return (
    <Card className="hover:shadow-lg transition-all">
      <CardHeader>
        <CardTitle className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <FileText className="h-5 w-5" />
            {document.name}
          </div>

          <Badge variant={getStatusVariant()}>
            {document.status}
          </Badge>
        </CardTitle>
      </CardHeader>

      <CardContent className="space-y-3">
        <div className="text-sm text-muted-foreground">
          <p>{document.fileName}</p>
          <p>Taille : {formatSize(document.fileSize)}</p>
          <p>
            Déposé le{" "}
            {new Date(document.uploadedAt).toLocaleDateString(
              "fr-FR"
            )}
          </p>
        </div>

        <div className="flex gap-2">
          <Button
            size="sm"
            onClick={() => onDownload(document.id)}
          >
            <Download className="mr-2 h-4 w-4" />
            Télécharger
          </Button>

          <Button
            variant="destructive"
            size="sm"
            onClick={() => onDelete(document.id)}
          >
            <Trash2 className="mr-2 h-4 w-4" />
            Supprimer
          </Button>
        </div>
      </CardContent>
    </Card>
  );
}