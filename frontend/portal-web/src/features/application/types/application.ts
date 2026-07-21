import { DocumentType } from "./document-type";


export interface Application {
    id: string;
    aidName: string;
    aidCode: string;
    status:
    | "GENERATED"
    | "READY_TO_COMPLETE"
    | "READY_TO_SUBMIT"
    | "SUBMITTED"
    | "ACCEPTED"
    | "REJECTED";
    createdAt: string;
    missingDocuments: DocumentType[];
}