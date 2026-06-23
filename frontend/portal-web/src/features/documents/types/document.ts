export interface Document {
  id: number;
  name: string;
  documentType: string;
  status: string;
  fileName: string;
  fileSize?: number;
  uploadedAt: string;
}