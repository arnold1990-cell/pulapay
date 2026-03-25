export type AuthResponse = {
  token: string;
  user: {
    name: string;
    email: string;
    phoneNumber: string;
    role: 'USER' | 'ADMIN';
  };
};
