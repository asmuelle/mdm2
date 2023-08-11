import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMeterImport } from '../meter-import.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../meter-import.test-samples';

import { MeterImportService } from './meter-import.service';

const requireRestSample: IMeterImport = {
  ...sampleWithRequiredData,
};

describe('MeterImport Service', () => {
  let service: MeterImportService;
  let httpMock: HttpTestingController;
  let expectedResult: IMeterImport | IMeterImport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MeterImportService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MeterImport', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const meterImport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(meterImport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MeterImport', () => {
      const meterImport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(meterImport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MeterImport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MeterImport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MeterImport', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMeterImportToCollectionIfMissing', () => {
      it('should add a MeterImport to an empty array', () => {
        const meterImport: IMeterImport = sampleWithRequiredData;
        expectedResult = service.addMeterImportToCollectionIfMissing([], meterImport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(meterImport);
      });

      it('should not add a MeterImport to an array that contains it', () => {
        const meterImport: IMeterImport = sampleWithRequiredData;
        const meterImportCollection: IMeterImport[] = [
          {
            ...meterImport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMeterImportToCollectionIfMissing(meterImportCollection, meterImport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MeterImport to an array that doesn't contain it", () => {
        const meterImport: IMeterImport = sampleWithRequiredData;
        const meterImportCollection: IMeterImport[] = [sampleWithPartialData];
        expectedResult = service.addMeterImportToCollectionIfMissing(meterImportCollection, meterImport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(meterImport);
      });

      it('should add only unique MeterImport to an array', () => {
        const meterImportArray: IMeterImport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const meterImportCollection: IMeterImport[] = [sampleWithRequiredData];
        expectedResult = service.addMeterImportToCollectionIfMissing(meterImportCollection, ...meterImportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const meterImport: IMeterImport = sampleWithRequiredData;
        const meterImport2: IMeterImport = sampleWithPartialData;
        expectedResult = service.addMeterImportToCollectionIfMissing([], meterImport, meterImport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(meterImport);
        expect(expectedResult).toContain(meterImport2);
      });

      it('should accept null and undefined values', () => {
        const meterImport: IMeterImport = sampleWithRequiredData;
        expectedResult = service.addMeterImportToCollectionIfMissing([], null, meterImport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(meterImport);
      });

      it('should return initial array if no MeterImport is added', () => {
        const meterImportCollection: IMeterImport[] = [sampleWithRequiredData];
        expectedResult = service.addMeterImportToCollectionIfMissing(meterImportCollection, undefined, null);
        expect(expectedResult).toEqual(meterImportCollection);
      });
    });

    describe('compareMeterImport', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMeterImport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMeterImport(entity1, entity2);
        const compareResult2 = service.compareMeterImport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMeterImport(entity1, entity2);
        const compareResult2 = service.compareMeterImport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMeterImport(entity1, entity2);
        const compareResult2 = service.compareMeterImport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
