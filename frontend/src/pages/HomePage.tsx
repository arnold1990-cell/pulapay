import { Link } from 'react-router-dom';

export default function HomePage() {
  return (
    <div className="container">
      <h1>PulaPay</h1>
      <p>Secure wallet and payment platform.</p>
      <div className="grid">
        <Link to="/login">Login</Link>
        <Link to="/register">Register</Link>
      </div>
    </div>
  );
}
