export const enum Access {
  FULL = 'FULL',
  LIMITED = 'LIMITED'
}

export interface IHB {
  id?: number;
  role?: string;
  access?: Access;
}

export const defaultValue: Readonly<IHB> = {};
