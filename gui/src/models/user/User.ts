import { Person } from '../Person';
import { Firm } from '../Firm';
import { UserRole } from './UserRole';

/**
 * User model interface.
 */
export interface User extends Person {
	username: string;
	password: string;
	role: UserRole;
	isLocked: boolean;
	isEnabled: boolean;
	firm?: Firm;
}
