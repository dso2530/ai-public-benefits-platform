export interface Profile {
  email: string;
  firstName: string;
  lastName: string;
  street?: string;
  postalCode?: string;
  city?: string;
  country?: string;
  housingStatus?: string;
  childrenCount?: number;
  singleParent?: boolean;
}