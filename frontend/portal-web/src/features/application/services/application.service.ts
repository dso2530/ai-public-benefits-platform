import { api } from "@/src/shared/api/axios";
import { Application } from "../types/application";
import { DocumentType } from "../types/document-type";

const BASE_URL = "/api/applications";

export async function getApplications(): Promise<Application[]> {
    const { data } = await api.get<Application[]>(BASE_URL);
    return data;
}

export async function getApplication(id: string): Promise<Application> {
    const { data } = await api.get<Application>(`${BASE_URL}/${id}`);
    return data;
}

export async function submitApplication(id: string): Promise<void> {
    await api.post(`${BASE_URL}/${id}/submit`);
}

export async function downloadPackage(id: string): Promise<{
    blob: Blob;
    filename: string;
}> {
    const response = await api.get(`/api/applications/${id}/document`, {
        responseType: "blob",
    });

    const contentDisposition =
        response.headers["content-disposition"];

    const filename =
        contentDisposition?.match(/filename\*?=(?:UTF-8'')?"?([^";]+)"?/)?.[1]
        ?? "application-package.zip";

    return {
        blob: response.data,
        filename: decodeURIComponent(filename),
    };
}


export async function uploadApplicationDocument(
    applicationId: string,
    documentType: DocumentType,
    file: File
) {
    const formData = new FormData();

    formData.append(
        "file",
        file
    );

    formData.append(
        "documentType",
        documentType
    );


    await api.post(
        `/api/applications/${applicationId}/documents`,
        formData,
        {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        }
    );
}