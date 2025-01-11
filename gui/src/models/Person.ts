import { BaseEntity } from './BaseEntity';

/**
 * Person interface.
 */
export interface Person extends BaseEntity {
	firstName: string;
	lastName: string;
	identificationNumber: string;
	address: string;
	phone: string;
	email: string;
}
