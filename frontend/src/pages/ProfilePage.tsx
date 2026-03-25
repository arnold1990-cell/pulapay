import Card from '../components/ui/Card';
import { useAuth } from '../context/AuthContext';

export default function ProfilePage() {
  const { user } = useAuth();

  return (
    <Card>
      <h3>Profile</h3>
      <div className="stack-sm">
        <p>
          <strong>Name:</strong> {user?.fullName ?? 'N/A'}
        </p>
        <p>
          <strong>Email:</strong> {user?.email ?? 'N/A'}
        </p>
        <p>
          <strong>Role:</strong> {user?.role ?? 'N/A'}
        </p>
        <p>
          <strong>Created At:</strong>{' '}
          {user?.createdAt ? new Date(user.createdAt).toLocaleString() : 'Not provided by backend'}
        </p>
      </div>
    </Card>
  );
}
