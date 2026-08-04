import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import SearchPage from "./pages/SearchPage";
import BookingPage from "./pages/BookingPage";
import BookingHistoryPage from "./pages/BookingHistory";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="/booking" element={<BookingPage />} />
        <Route path="/history" element={<BookingHistoryPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;