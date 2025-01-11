import { BaseEntity } from '../BaseEntity';
import { Currency } from './Currency';
import { Firm } from '../Firm';

/**
 * Appointment type model interface.
 */
export interface AppointmentType extends BaseEntity {
	name: string;
	durationMinutes: number;
	price: number;
	colorCode: string;
	currency: Currency;
	firm?: Firm;
}
