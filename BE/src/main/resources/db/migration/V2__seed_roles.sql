INSERT INTO vai_tro (ten_vai_tro, mo_ta)
VALUES
    ('Admin', 'Quan tri he thong'),
    ('NhanVien', 'Nhan vien van hanh tong hop'),
    ('Staff', 'Nhan vien xu ly don hang'),
    ('Kho', 'Nhan vien kho'),
    ('Sales', 'Nhan vien ban hang')
ON CONFLICT (ten_vai_tro) DO NOTHING;
