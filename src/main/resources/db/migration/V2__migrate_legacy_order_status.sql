UPDATE orders SET status = 'CREATED'  WHERE status = 'PENDING';
UPDATE orders SET status = 'CANCELED' WHERE status = 'CANCELLED';