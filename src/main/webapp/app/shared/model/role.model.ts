export interface IRole {
  id?: number;
  role?: string;
  accessId?: number;
  roleId?: string;
}

export const defaultValue: Readonly<IRole> = {};
