import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IContract {
  id: number;
  name?: string | null;
  customerContactName?: string | null;
  customerContactAddresslines?: string | null;
  customerPurchaseNumber?: string | null;
  kwiqlyOrderNumber?: string | null;
  basePricePerMonth?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
