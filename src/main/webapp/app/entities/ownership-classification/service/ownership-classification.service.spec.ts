import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOwnershipClassification } from '../ownership-classification.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../ownership-classification.test-samples';

import { OwnershipClassificationService } from './ownership-classification.service';

const requireRestSample: IOwnershipClassification = {
  ...sampleWithRequiredData,
};

describe('OwnershipClassification Service', () => {
  let service: OwnershipClassificationService;
  let httpMock: HttpTestingController;
  let expectedResult: IOwnershipClassification | IOwnershipClassification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OwnershipClassificationService);
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

    it('should create a OwnershipClassification', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ownershipClassification = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ownershipClassification).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OwnershipClassification', () => {
      const ownershipClassification = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ownershipClassification).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OwnershipClassification', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OwnershipClassification', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OwnershipClassification', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOwnershipClassificationToCollectionIfMissing', () => {
      it('should add a OwnershipClassification to an empty array', () => {
        const ownershipClassification: IOwnershipClassification = sampleWithRequiredData;
        expectedResult = service.addOwnershipClassificationToCollectionIfMissing([], ownershipClassification);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ownershipClassification);
      });

      it('should not add a OwnershipClassification to an array that contains it', () => {
        const ownershipClassification: IOwnershipClassification = sampleWithRequiredData;
        const ownershipClassificationCollection: IOwnershipClassification[] = [
          {
            ...ownershipClassification,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOwnershipClassificationToCollectionIfMissing(
          ownershipClassificationCollection,
          ownershipClassification
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OwnershipClassification to an array that doesn't contain it", () => {
        const ownershipClassification: IOwnershipClassification = sampleWithRequiredData;
        const ownershipClassificationCollection: IOwnershipClassification[] = [sampleWithPartialData];
        expectedResult = service.addOwnershipClassificationToCollectionIfMissing(
          ownershipClassificationCollection,
          ownershipClassification
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ownershipClassification);
      });

      it('should add only unique OwnershipClassification to an array', () => {
        const ownershipClassificationArray: IOwnershipClassification[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const ownershipClassificationCollection: IOwnershipClassification[] = [sampleWithRequiredData];
        expectedResult = service.addOwnershipClassificationToCollectionIfMissing(
          ownershipClassificationCollection,
          ...ownershipClassificationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ownershipClassification: IOwnershipClassification = sampleWithRequiredData;
        const ownershipClassification2: IOwnershipClassification = sampleWithPartialData;
        expectedResult = service.addOwnershipClassificationToCollectionIfMissing([], ownershipClassification, ownershipClassification2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ownershipClassification);
        expect(expectedResult).toContain(ownershipClassification2);
      });

      it('should accept null and undefined values', () => {
        const ownershipClassification: IOwnershipClassification = sampleWithRequiredData;
        expectedResult = service.addOwnershipClassificationToCollectionIfMissing([], null, ownershipClassification, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ownershipClassification);
      });

      it('should return initial array if no OwnershipClassification is added', () => {
        const ownershipClassificationCollection: IOwnershipClassification[] = [sampleWithRequiredData];
        expectedResult = service.addOwnershipClassificationToCollectionIfMissing(ownershipClassificationCollection, undefined, null);
        expect(expectedResult).toEqual(ownershipClassificationCollection);
      });
    });

    describe('compareOwnershipClassification', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOwnershipClassification(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOwnershipClassification(entity1, entity2);
        const compareResult2 = service.compareOwnershipClassification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOwnershipClassification(entity1, entity2);
        const compareResult2 = service.compareOwnershipClassification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOwnershipClassification(entity1, entity2);
        const compareResult2 = service.compareOwnershipClassification(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
