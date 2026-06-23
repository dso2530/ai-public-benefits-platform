
import { api } from "@/src/shared/api/axios";
import { Document } from "../types/document";


export const getDocuments = async (): Promise<Document[]> => {
  const response = await api.get("/api/documents");
  return response.data;
};

export const uploadDocument = async (
  file: File,
  type: string
): Promise<Document> => {
  const formData = new FormData();

  formData.append("file", file);
  formData.append("documentType", type);

  const response = await api.post(
    "/api/documents",
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );

  return response.data;
};

export const deleteDocument = async (
  id: number
): Promise<void> => {
  await api.delete(`/api/documents/${id}`);
};

export const downloadDocument = (
  id: number
): void => {
  window.open(
    `/api/documents/${id}/download`,
    "_blank"
  );
};