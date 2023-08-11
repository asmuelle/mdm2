import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOwnershipProperty } from '../ownership-property.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ownership-property.test-samples';

import { OwnershipPropertyService } from './ownership-property.service';

const requireRestSample: IOwnershipProperty = {
  ...sampleWithRequiredData,
};

describe('OwnershipProperty Service', () => {
  let service: OwnershipPropertyService;
  let httpMock: HttpTestingController;
  let expectedResult: IOwnershipProperty | IOwnershipProperty[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OwnershipPropertyService);
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

    it('should create a OwnershipProperty', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ownershipProperty = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ownershipProperty).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OwnershipProperty', () => {
      const ownershipProperty = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ownershipProperty).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OwnershipProperty', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OwnershipProperty', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OwnershipProperty', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOwnershipPropertyToCollectionIfMissing', () => {
      it('should add a OwnershipProperty to an empty array', () => {
        const ownershipProperty: IOwnershipProperty = sampleWithRequiredData;
        expectedResult = service.addOwnershipPropertyToCollectionIfMissing([], ownershipProperty);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ownershipProperty);
      });

      it('should not add a OwnershipProperty to an array that contains it', () => {
        const ownershipProperty: IOwnershipProperty = sampleWithRequiredData;
        const ownershipPropertyCollection: IOwnershipProperty[] = [
          {
            ...ownershipProperty,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOwnershipPropertyToCollectionIfMissing(ownershipPropertyCollection, ownershipProperty);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OwnershipProperty to an array that doesn't contain it", () => {
        const ownershipProperty: IOwnershipProperty = sampleWithRequiredData;
        const ownershipPropertyCollection: IOwnershipProperty[] = [sampleWithPartialData];
        expectedResult = service.addOwnershipPropertyToCollectionIfMissing(ownershipPropertyCollection, ownershipProperty);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ownershipProperty);
      });

      it('should add only unique OwnershipProperty to an array', () => {
        const ownershipPropertyArray: IOwnershipProperty[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ownershipPropertyCollection: IOwnershipProperty[] = [sampleWithRequiredData];
        expectedResult = service.addOwnershipPropertyToCollectionIfMissing(ownershipPropertyCollection, ...ownershipPropertyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ownershipProperty: IOwnershipProperty = sampleWithRequiredData;
        const ownershipProperty2: IOwnershipProperty = sampleWithPartialData;
        expectedResult = service.addOwnershipPropertyToCollectionIfMissing([], ownershipProperty, ownershipProperty2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ownershipProperty);
        expect(expectedResult).toContain(ownershipProperty2);
      });

      it('should accept null and undefined values', () => {
        const ownershipProperty: IOwnershipProperty = sampleWithRequiredData;
        expectedResult = service.addOwnershipPropertyToCollectionIfMissing([], null, ownershipProperty, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ownershipProperty);
      });

      it('should return initial array if no OwnershipProperty is added', () => {
        const ownershipPropertyCollection: IOwnershipProperty[] = [sampleWithRequiredData];
        expectedResult = service.addOwnershipPropertyToCollectionIfMissing(ownershipPropertyCollection, undefined, null);
        expect(expectedResult).toEqual(ownershipPropertyCollection);
      });
    });

    describe('compareOwnershipProperty', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOwnershipProperty(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOwnershipProperty(entity1, entity2);
        const compareResult2 = service.compareOwnershipProperty(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOwnershipProperty(entity1, entity2);
        const compareResult2 = service.compareOwnershipProperty(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOwnershipProperty(entity1, entity2);
        const compareResult2 = service.compareOwnershipProperty(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
