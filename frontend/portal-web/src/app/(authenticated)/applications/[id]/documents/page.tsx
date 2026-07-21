import { MissingDocuments } from "../../../../../features/application/components/missing-documents";
import { getApplication } from "../../../../../features/application/services/application.service";
type Props = {
    params: Promise<{
        id: string;
    }>;
};

export default async function Page({ params }: Props) {
    const { id } = await params;

    const application = await getApplication(id);

    return (
        <MissingDocuments
            applicationId={id}
            missingDocuments={application.missingDocuments ?? []}
        />
    );
}