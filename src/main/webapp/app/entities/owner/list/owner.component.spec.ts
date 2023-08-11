import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OwnerService } from '../service/owner.service';

import { OwnerComponent } from './owner.component';

describe('Owner Management Component', () => {
  let comp: OwnerComponent;
  let fixture: ComponentFixture<OwnerComponent>;
  let service: OwnerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'owner', component: OwnerComponent }]), HttpClientTestingModule, OwnerComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(OwnerComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OwnerComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OwnerService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.owners?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ownerService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOwnerIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOwnerIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
